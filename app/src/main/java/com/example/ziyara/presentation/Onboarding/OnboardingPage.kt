package com.example.ziyara.presentation.Onboarding

import androidx.annotation.DrawableRes
import com.example.ziyara.R

data class OnboardingPage(
    @DrawableRes val image: Int,
    val title: String,
    val description: String
)

val onboardingPages = listOf(
    OnboardingPage(
        image = R.drawable.onboarding_discover_egypt,
        title = "Discover Egypt",
        description = "Explore the most iconic historical sites and hidden gems across Egypt. Your adventure starts here."
    ),
    OnboardingPage(
        image = R.drawable.onboarding_plan_journey,
        title = "Plan Your Journey",
        description = "Save your favorite places, plan your visits, and organize your perfect journey with ease."
    ),
    OnboardingPage(
        image = R.drawable.onboarding_never_miss_event,
        title = "Never Miss an Event",
        description = "Get real-time updates about events, openings, and travel tips to make the most of your experience."
    )
)