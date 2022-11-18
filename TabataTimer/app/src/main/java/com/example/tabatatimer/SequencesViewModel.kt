package com.example.tabatatimer

import android.app.Application
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import java.io.*
import java.lang.Exception
import kotlin.random.Random

class SequencesViewModel(app: Application) : AndroidViewModel(app) {
    private val _sequencesList: MutableLiveData<MutableList<Sequence>> =
        MutableLiveData<MutableList<Sequence>>(mutableListOf())

    val sequencesList: LiveData<MutableList<Sequence>> get() =
        _sequencesList

    private fun getRandomColor(): Int {
        val red = Random.nextInt() % 50 + 130
        val green = Random.nextInt() % 50 + 130
        val blue = Random.nextInt() % 50 + 130
        return Color.rgb(red, green, blue)
    }

    fun loadSequencesFromFile() {
        // load the sequences list from .json file
        val context = getApplication<Application>().applicationContext

        val file = File(context.filesDir, "sequences.json")

        if (file.exists()) {
            Log.d("SequencesViewModel", "file exists")
            // read it

            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)
            val contents: List<String> = bufferedReader.readLines()
            bufferedReader.close()
            var contentsStr = ""

            for (line in contents) {
                contentsStr += line + "\n"
            }

            try {
                val sequencesArray = Gson().fromJson(contentsStr, Array<Sequence>::class.java)

                for (seq in sequencesArray) {
                    _sequencesList.value!!.add(seq)
                }
            }
            catch (e: Exception) {
                Log.d("SequencesViewModel", "sequences.json is empty or corrupted!")
            }
        }
        else {
            file.createNewFile()
            Log.d("SequencesViewModel", "file does not exist. creating a new file")
        }
    }

    fun writeSequencesToFile() {
        val sequencesJsonString =
            Gson().toJson(_sequencesList.value!!.toTypedArray())
        Log.d("SequencesViewModel", sequencesJsonString)

        val context = getApplication<Application>().applicationContext

        val file = File(context.filesDir, "sequences.json")
        file.createNewFile()
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(sequencesJsonString)
        bufferedWriter.close()
    }

    init {
        /*val colorsList = mutableListOf<Int>()

        for (i in 0..4) {
            colorsList.add(getRandomColor())
        }

        Log.d("SequencesViewModel", colorsList.toString())*/

        loadSequencesFromFile()

        /*for (i in 1..1) {
            _sequencesList.value!!.add(
                Sequence(
                    //getRandomColor(),
                    Color.parseColor("#FF8FB391"),
                    "basic training",
                    2,
                    listOf(
                        Phase("warmup",20),
                        Phase("work", 10),
                        Phase("rest", 10),
                        Phase("work", 10),
                        Phase("rest", 10),
                        Phase("work", 10),
                        Phase("rest", 10),
                        Phase("cooldown", 20)
                    )
                )
            )
        }

        writeSequencesToFile()*/
    }

    fun deleteSequence(position: Int) {
        _sequencesList.value!!.removeAt(position)
    }

    fun updateSequence(position: Int, seq: Sequence) {
        _sequencesList.value!![position] = seq
    }
}