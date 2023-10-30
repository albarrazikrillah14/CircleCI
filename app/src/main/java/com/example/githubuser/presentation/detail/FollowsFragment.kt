package com.example.githubuser.presentation.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentFollowsBinding
import com.medomeckz.core.data.Resource
import com.medomeckz.core.domain.model.Users
import com.medomeckz.core.ui.UserAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class FollowsFragment : Fragment() {
    private lateinit var binding: FragmentFollowsBinding
    private lateinit var adapter: UserAdapter
    private val viewModel: DetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter()

        var position = 0
        var username = ""

        arguments?.let {
            position = it.getInt(ARG_POSITION, 0)
            username = it.getString(ARG_USERNAME).toString()
            showToast(username)
        }

        if (position == 1) {
            viewModel.getUserFollowers(username).observe(viewLifecycleOwner) {
                showData(it)
            }
        } else {
            viewModel.getUserFollowing(username).observe(viewLifecycleOwner) {
                showData(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.rvGithub.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onPause() {
        super.onPause()
        binding.rvGithub.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0
        )
    }

    private fun setAdapter() {
        adapter = UserAdapter()

        binding.apply {
            rvGithub.layoutManager = LinearLayoutManager(requireActivity())
            rvGithub.setHasFixedSize(false)
            rvGithub.adapter = adapter
        }

    }

    private fun showData(data: Resource<List<Users>>?) {
        if (data != null) {
            when (data) {
                is Resource.Loading -> {
                    stateEmpty(false)
                    stateError(false)
                }

                is Resource.Success -> {
                    if (data.data.isNullOrEmpty()) {
                        stateEmpty(true)
                    } else {
                        stateEmpty(false)
                        adapter.submitList(data.data)
                        adapter.onItemClick = { selectedData ->
                            val intent = Intent(requireActivity(), DetailActivity::class.java)
                            intent.putExtra(DetailActivity.USER, selectedData.login)
                            startActivity(intent)
                        }
                    }
                    stateError(false)
                }

                is Resource.Error -> {
                    stateEmpty(false)
                    stateError(true)
                }
            }
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = "section_username"
    }
}