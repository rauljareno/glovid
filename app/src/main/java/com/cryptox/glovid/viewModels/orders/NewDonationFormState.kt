package com.cryptox.glovid.viewModels.orders

/**
 * Data validation state of the new donation form.
 */
data class NewDonationFormState(val descError: Int? = null,
                                val isDataValid: Boolean = false)
