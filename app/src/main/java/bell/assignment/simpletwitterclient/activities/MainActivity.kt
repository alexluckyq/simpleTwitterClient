package bell.assignment.simpletwitterclient.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import bell.assignment.simpletwitterclient.R
import bell.assignment.simpletwitterclient.fragments.MapFragment
import bell.assignment.simpletwitterclient.fragments.SearchFragment
import bell.assignment.simpletwitterclient.managers.cache.CacheManager
import bell.assignment.simpletwitterclient.managers.location.LocationManager
import bell.assignment.simpletwitterclient.managers.location.LocationManagerImpl
import bell.assignment.simpletwitterclient.managers.location.model.LocationRequest
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val TAG_MAP = "TAG_MAP"
    private val TAG_SEARCH = "TAG_SEARCH"

    private var currentFragment: Fragment? = null
    private var currentTabId: Int = -1

    private val locationManager: LocationManager =LocationManagerImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener { item ->
            bottomNavigationItemSelected(item)
            true
        }

        bottomNavigationItemSelected(navView.menu.getItem(0))
        startUpdateLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        locationManager.onActivityResult(requestCode, resultCode, data)?:
        super.onActivityResult(requestCode, resultCode, data)
    }

    protected fun bottomNavigationItemSelected(item: MenuItem) {
        selectFragment(item)
    }

    private fun selectFragment(item: MenuItem) {
        var frag: Fragment? = null
        var tag: String? = null

        when (item.itemId) {
            R.id.navigation_map -> {
                tag = TAG_MAP
                frag = supportFragmentManager.findFragmentByTag(tag)
                if (frag == null) {
                    frag = MapFragment()
                }
            }
            R.id.navigation_search -> {
                tag = TAG_SEARCH
                frag = supportFragmentManager.findFragmentByTag(tag)
                if (frag == null) {
                    frag = SearchFragment()
                }
            }
        }

        if (frag != null && frag !== currentFragment) {
            val ft = supportFragmentManager.beginTransaction()
            currentFragment?.let {
                if (it.isAdded && it.isVisible) {
                    ft.hide(it)
                }
            }

            if (!frag.isAdded) {
                ft.add(R.id.container, frag, tag)
            } else {
                ft.show(frag)
            }
            ft.commit()
        }

        currentFragment = frag
        currentTabId = item.itemId
    }

    fun startUpdateLocation() {
        locationManager.startUpdateLocation(
            this,
            LocationRequest(),
            { CacheManager.setCurrentLocation(location = it) }
        )
    }

}
