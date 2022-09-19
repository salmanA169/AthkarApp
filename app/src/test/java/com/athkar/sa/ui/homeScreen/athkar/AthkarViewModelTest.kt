package com.athkar.sa.ui.homeScreen.athkar

import com.athkar.sa.db.entity.Athkar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class AthkarViewModelTest{

    @Test
    fun readFileAndSpiltTexts(){
        val file = File("src//main///assets//athkar.json")
        val reader = file.reader()
        val jsonReader = JsonReader(reader).use {
            val token = object : TypeToken<List<Athkar>>(){}.type
           Gson().fromJson<List<Athkar>>(it,token)
        }
        val filterToListHadit = jsonReader.filter {
            val hadithContent = it.hadithContent
            if (hadithContent != null){
                hadithContent.listHadith!=null
            }else{
                false
            }
        }
        val getList = filterToListHadit.first()
        getList.hadithContent!!.listHadith!!.forEach {
            val spiltText = it.indexOf("(")
            val endSpilt = it.indexOf(")")
            if (spiltText != -1 && endSpilt != -1){
                println("has")
                println(spiltText)
                println(endSpilt)
            }
            println(spiltText)
            println()
        }
    }


    @Test
    fun testRegex(){
        val text = "salman saleh alamoudi find uni 0559929281"
        val regex = "salman salehhh".toRegex()

    }

}