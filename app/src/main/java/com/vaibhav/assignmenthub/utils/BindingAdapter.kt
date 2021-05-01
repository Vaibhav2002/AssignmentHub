package com.vaibhav.assignmenthub.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.vaibhav.assignmenthub.data.models.Assignment

@BindingAdapter("setAssignmentName")
fun TextView.setAssignmentName(assignment: Assignment) {
    text = "${assignment.subject} : ${assignment.name}"
}

@BindingAdapter("setOnBoardingImage")
fun ImageView.setOnBoardingImage(image: Int) {
    Glide.with(this).load(image).into(this)
}