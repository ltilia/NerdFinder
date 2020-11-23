package com.bignerdranch.android.nerdfinder.controller

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.nerdfinder.R
import com.bignerdranch.android.nerdfinder.listener.VenueSearchListener
import com.bignerdranch.android.nerdfinder.model.TokenStore
import com.bignerdranch.android.nerdfinder.model.Venue
import com.bignerdranch.android.nerdfinder.view.VenueListAdapter
import com.bignerdranch.android.nerdfinder.web.DataManager

class VenueListFragment : Fragment(), VenueSearchListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var venueListAdapter: VenueListAdapter
    private var venueList = emptyList<Venue>()
    private lateinit var tokenStore: TokenStore
    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        tokenStore = TokenStore.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
                R.layout.fragment_venue_list, container, false)
        recyclerView = view.findViewById(R.id.venueListRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        venueListAdapter = VenueListAdapter(venueList)
        recyclerView.adapter = venueListAdapter
        return view
    }

    override fun onStart() {
        super.onStart()
        dataManager = DataManager.get()
        dataManager.addVenueSearchListener(this)
        dataManager.fetchVenueSearch()
    }

    override fun onStop() {
        super.onStop()
        dataManager.removeVenueSearchListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (tokenStore.accessToken == null) {
            inflater.inflate(R.menu.menu_sign_in, menu)
        } else {
            inflater.inflate(R.menu.menu_sign_out, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_in -> {
                val authenticationIntent = AuthenticationActivity.newIntent(requireContext())
                startActivityForResult(authenticationIntent, AUTHENTICATION_ACTIVITY_REQUEST)
                return true
            }
            R.id.sign_out -> {
                tokenStore.accessToken = null
                requireActivity().invalidateOptionsMenu()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTHENTICATION_ACTIVITY_REQUEST) {
            requireActivity().invalidateOptionsMenu()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onVenueSearchFinished() {
        venueList = dataManager.venueList
        venueListAdapter.venueList = venueList
    }

    companion object {
        private const val AUTHENTICATION_ACTIVITY_REQUEST = 0

        fun newInstance(): VenueListFragment {
            return VenueListFragment()
        }
    }
}
