package com.android.test.fragment


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders

import com.android.test.R
import com.android.test.local_db.entity.Category
import com.android.test.view_model.CategoryViewModel
import kotlinx.android.synthetic.main.fragment_category_add.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class CategoryAddFragment : DialogFragment() {

    var mViewModel: CategoryViewModel? = null

    companion object {
        fun newInstance(): CategoryAddFragment {
            val frag = CategoryAddFragment()
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialogBuilder = Dialog(this.activity!!)
        alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialogBuilder.setContentView(R.layout.fragment_category_add)
        alertDialogBuilder.show()

        alertDialogBuilder.btn_save.setOnClickListener { view ->
            if (mViewModel?.checkValidity(
                    alertDialogBuilder.et_name.text.toString(),
                    alertDialogBuilder.et_description.text.toString()
                )!!
            ) {

                mViewModel!!.insertCategory(
                    Category(
                        UUID.randomUUID().toString(),
                        alertDialogBuilder.et_name.text.toString(),
                        alertDialogBuilder.et_description.text.toString()
                    )
                )

                alertDialogBuilder.dismiss()
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.text_alert_empty_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return alertDialogBuilder

    }


}
