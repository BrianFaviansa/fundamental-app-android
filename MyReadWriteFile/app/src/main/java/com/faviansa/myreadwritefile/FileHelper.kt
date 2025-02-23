package com.faviansa.myreadwritefile

import android.content.Context

internal object FileHelper {
    fun writeToFile(fileModel: FileModel, context: Context) {
        context.openFileOutput(fileModel.fileName, Context.MODE_PRIVATE).use {
            it.write(fileModel.data?.toByteArray())
        }
    }

    fun readFromFile(context: Context, fileName: String): FileModel {
        val fileModel = FileModel()
        fileModel.fileName = fileName
        fileModel.data = context.openFileInput(fileName).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some$text\n"
            }
        }
        return fileModel
    }
}