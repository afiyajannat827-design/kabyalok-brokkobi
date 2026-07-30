package com.example.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

data class BengaliFontOption(
    val name: String,
    val category: String, // "সব", "ক্যালিগ্রাফি", "আধুনিক", "ক্লাসিক", "হাতের লেখা", "কাব্যিক"
    val fontFamily: FontFamily,
    val fontWeight: FontWeight = FontWeight.Normal,
    val fontStyle: FontStyle = FontStyle.Normal
)

object FontHelper {
    val fontOptions = listOf(
        BengaliFontOption("সোনার তরী", "ক্লাসিক", FontFamily.Serif, FontWeight.Bold),
        BengaliFontOption("কাব্যলোক ক্যালিগ্রাফি", "ক্যালিগ্রাফি", FontFamily.Cursive, FontWeight.Medium, FontStyle.Italic),
        BengaliFontOption("আধুনিক কবিতা", "আধুনিক", FontFamily.SansSerif, FontWeight.Normal),
        BengaliFontOption("হাতে লেখা পান্ডুলিপি", "হাতের লেখা", FontFamily.Monospace, FontWeight.Normal, FontStyle.Italic),
        BengaliFontOption("রবীন্দ্র শৈলী", "ক্লাসিক", FontFamily.Serif, FontWeight.Normal, FontStyle.Italic),
        BengaliFontOption("নজরুল বার্তা", "আধুনিক", FontFamily.SansSerif, FontWeight.Bold),
        BengaliFontOption("জীবনানন্দ নিবিড়", "ক্যালিগ্রাফি", FontFamily.Cursive, FontWeight.Bold),
        BengaliFontOption("বিদ্রোহী বর্ণালী", "হাতের লেখা", FontFamily.Monospace, FontWeight.Bold),
        BengaliFontOption("মেঘদূত ক্যালিগ্রাফি", "ক্যালিগ্রাফি", FontFamily.Cursive, FontWeight.SemiBold),
        BengaliFontOption("অনুপম বার্তা", "আধুনিক", FontFamily.SansSerif, FontWeight.Medium),
        BengaliFontOption("প্রাচীন পান্ডুলিপি", "ক্লাসিক", FontFamily.Serif, FontWeight.SemiBold, FontStyle.Italic),
        BengaliFontOption("বসন্ত বাতাস", "কাব্যিক", FontFamily.Serif, FontWeight.Medium),
        BengaliFontOption("শারদ লিপি", "হাতের লেখা", FontFamily.Monospace, FontWeight.Medium, FontStyle.Italic),
        BengaliFontOption("শঙ্খচিল", "কাব্যিক", FontFamily.Default, FontWeight.Medium),
        BengaliFontOption("বটপত্র", "ক্লাসিক", FontFamily.Serif, FontWeight.ExtraBold),
        BengaliFontOption("নীলকণ্ঠ", "আধুনিক", FontFamily.SansSerif, FontWeight.SemiBold),
        BengaliFontOption("চিত্রা নদীর তীরে", "ক্যালিগ্রাফি", FontFamily.Cursive, FontWeight.Normal, FontStyle.Italic),
        BengaliFontOption("কবিতার খাতা", "হাতের লেখা", FontFamily.Monospace, FontWeight.SemiBold),
        BengaliFontOption("শ্রাবণ মেঘ", "কাব্যিক", FontFamily.Default, FontWeight.Bold, FontStyle.Italic),
        BengaliFontOption("উষসী রোদ", "আধুনিক", FontFamily.SansSerif, FontWeight.Light),
        BengaliFontOption("হিমালয় দর্পন", "ক্লাসিক", FontFamily.Serif, FontWeight.Normal),
        BengaliFontOption("পদ্মাবতী", "ক্যালিগ্রাফি", FontFamily.Cursive, FontWeight.Bold, FontStyle.Italic)
    )

    fun getFontByName(name: String): BengaliFontOption {
        return fontOptions.firstOrNull { it.name == name } ?: fontOptions[0]
    }
}

