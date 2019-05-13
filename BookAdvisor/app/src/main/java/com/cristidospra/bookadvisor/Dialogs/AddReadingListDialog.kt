package com.cristidospra.bookadvisor.Dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils

class AddReadingListDialog(private val currentContext: Context,
                           private val onSave: (String) -> Unit

) : Dialog(currentContext), View.OnClickListener {

    private lateinit var titleEditText: EditText
    private lateinit var cancelButton: TextView
    private lateinit var addButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.add_reading_list_dialog)

        inflateViews()

        cancelButton.setOnClickListener(this)
        addButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.add_readinglist_dialog_cancel_button -> {

                Utils.closeKeyboard(currentContext as Activity, titleEditText)
            }
            R.id.add_readinglist_dialog_add_button -> {

                Utils.closeKeyboard(currentContext as Activity, titleEditText)
                onSave(titleEditText.text.toString())
            }
        }

        Utils.closeKeyboard(currentContext as Activity, titleEditText)
        dismiss()
    }

    private fun inflateViews() {

        titleEditText = findViewById(R.id.add_readinglist_dialog_content)
        cancelButton = findViewById(R.id.add_readinglist_dialog_cancel_button)
        addButton = findViewById(R.id.add_readinglist_dialog_add_button)
    }

}