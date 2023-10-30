package com.example.githubuser.presentation.main

import android.app.SearchManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.presentation.detail.DetailActivity
import com.example.githubuser.presentation.developer.DeveloperActivity
import com.medomeckz.core.data.Resource
import com.medomeckz.core.ui.UserAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private val viewModel: MainViewModel by viewModel()
    private lateinit var broadcastReceiver: BroadcastReceiver



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUser("albarra")
        setAdapter()

//        preferences()

//        viewModel.resultUser.observe(this) {
//            when(it) {
//                is Result.Success<*> -> {
//                    adapter.setData(it.data as MutableList<ItemsItem>)
//                }
//                is Result.Error -> {
//                    Toast.makeText(
//                        this@MainActivity,
//                        it.exception.message.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                is Result.Loading -> {
//                    binding.progressBar.isVisible = it.isLoading
//                }
//            }
//        }
//
//        viewModel.getUser("albarra")
    }

//    private fun preferences() {
//        val pref = SettingPreferences.getInstance(dataStore)
//        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref))[SettingViewModel::class.java]
//        settingViewModel.getThemeSettings().observe(this) {
//            if(it) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            }else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//        }
//    }
private fun registerBroadCastReceiver() {
    broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_POWER_CONNECTED -> {
                    binding.tvPowerStatus.text = getString(R.string.power_connected)
                }
                Intent.ACTION_POWER_DISCONNECTED -> {
                    binding.tvPowerStatus.text = getString(R.string.power_disconnected)
                }
            }
        }
    }
    val intentFilter = IntentFilter()
    intentFilter.apply {
        addAction(Intent.ACTION_POWER_CONNECTED)
        addAction(Intent.ACTION_POWER_DISCONNECTED)
    }
    registerReceiver(broadcastReceiver, intentFilter)
}

    override fun onStart() {
        super.onStart()
        registerBroadCastReceiver()
    }
    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.developer -> {
                val intent = Intent(this@MainActivity, DeveloperActivity::class.java)
                startActivity(intent)
            }
            R.id.favorite -> {
                val intent = Intent(
                    this@MainActivity,
                    Class.forName("com.medomeckz.favorite.presentation.FavoriteActivity")
                )
                startActivity(intent)
            }
        }
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getUser(query.toString())
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return true
    }

    fun getUser(query: String) {
        viewModel.getUsersByUsername(query).observe(this){users ->
            when(users) {
                is Resource.Loading -> {
                    stateLoading(true)
                    stateEmpty(false)
                    stateError(false)
                }
                is Resource.Success -> {
                    stateLoading(false)
                    showToast(query)
                    adapter.submitList(users.data)
                    adapter.onItemClick = {
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.USER, it.login)
                        startActivity(intent)
                    }
                    stateError(false)
                }
                is Resource.Error -> {
                    stateLoading(false)
                    stateEmpty(false)
                    stateError(true)
                }
            }
        }
    }

    private fun setAdapter() {
        adapter = UserAdapter()
        binding.apply {
            rvGithub.layoutManager = LinearLayoutManager(this@MainActivity)
            rvGithub.setHasFixedSize(true)
            rvGithub.adapter = adapter
        }
    }
    private fun stateLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun stateEmpty(isEmpty: Boolean) {
      if(isEmpty) {
          binding.tvState.visibility = View.VISIBLE
      } else {
          binding.tvState.visibility = View.GONE
      }

      binding.tvState.text = getString(R.string.empty_data)
    }

    private fun stateError(isError: Boolean) {
        if(isError) {
            binding.tvState.visibility = View.VISIBLE
        } else {
            binding.tvState.visibility = View.GONE
        }

        binding.tvState.text = getString(R.string.error)
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}