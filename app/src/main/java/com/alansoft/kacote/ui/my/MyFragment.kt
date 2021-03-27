package com.alansoft.kacote.ui.my

import com.alansoft.kacote.ui.main.PlaceholderFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by LEE MIN KYU on 2021/03/25
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
@AndroidEntryPoint
class MyFragment : PlaceholderFragment() {
    companion object {
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(): PlaceholderFragment {
            return MyFragment()
        }
    }
}