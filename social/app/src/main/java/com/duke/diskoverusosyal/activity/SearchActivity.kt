package com.duke.diskoverusosyal.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.search.SearchModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverulifestyle.adapter.RecyclerView_Search_Adapter
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        ivSearchBack.setOnClickListener { finish() }
        etSearch.requestFocus()
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val search = p0.toString()
                getData(search)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun getData(search: String) {
        pbSearch.visibility = View.VISIBLE
        tvSearchNoData.visibility = View.GONE
        rvSearch.visibility = View.GONE
        val call = ApiClient().getClient().getSearchResult(search)
        call.enqueue(object : Callback<SearchModel> {
            override fun onResponse(call: Call<SearchModel>?, response: Response<SearchModel>?) {
                try {
                    pbSearch.visibility = View.GONE
                    if (response!!.body() != null && response.isSuccessful) {
                        if (response.body()?.status!!) {
                            val data = response.body()?.response?.data
                            tvSearchNoData.visibility = View.GONE
                            rvSearch.visibility = View.VISIBLE
                            rvSearch.layoutManager = LinearLayoutManager(this@SearchActivity)
                            rvSearch.adapter = RecyclerView_Search_Adapter(data, this@SearchActivity)
                        }else{
                            tvSearchNoData.visibility = View.VISIBLE
                            rvSearch.visibility = View.GONE
                        }

                    } else {
                        tvSearchNoData.visibility = View.VISIBLE
                        tvSearchNoData.text = resources.getString(R.string.no_response)
                        rvSearch.visibility = View.GONE
                    }
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }

            override fun onFailure(call: Call<SearchModel>?, t: Throwable?) {
                try {
                    pbSearch.visibility = View.GONE
                    tvSearchNoData.visibility = View.VISIBLE
                    tvSearchNoData.text = resources.getString(R.string.api_failure)
                    rvSearch.visibility = View.GONE
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }
        })
    }
}
