package com.example.cateringapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CustomerHomeViewModel : ViewModel() {
    private val database = FirebaseDatabaseRepository.getInstance()
    private val _bagCount = MutableLiveData<Int>().apply {
        value = 0
    }

    private val bagCount = _bagCount

    fun getBagCount() : LiveData<Int> {
        database.bag
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        bagCount.value = snapshot.childrenCount.toInt()
                    }

                    override fun onCancelled(error: DatabaseError) = Unit
                })
        return bagCount
    }
}