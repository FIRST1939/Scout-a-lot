package com.frcteam1939.scouting2026.android.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset

/**
 * iOS-style (Cupertino) page transitions for Jetpack Compose NavHost.
 *
 * Extracted from alexzhirkevich/compose-cupertino.
 *
 * Usage:
 * ```
 * NavHost(
 *     navController = navController,
 *     startDestination = "home",
 *     enterTransition = { CupertinoTransitions.enterTransition },
 *     exitTransition = { CupertinoTransitions.exitTransition },
 *     popEnterTransition = { CupertinoTransitions.popEnterTransition },
 *     popExitTransition = { CupertinoTransitions.popExitTransition },
 * ) { ... }
 * ```
 */
object CupertinoTransitions {

    // iOS UINavigationController bezier curve
    private val CupertinoEasing = CubicBezierEasing(0.2833f, 0.99f, 0.31833f, 0.99f)

    // Transition duration (ms)
    private const val TransitionDuration = 500

    // Background page offset ratio (iOS slides the underlying page ~25% of screen width)
    private const val SlideFactor = 0.25f

    // compose-cupertino uses a black scrim with alpha ~= 0.25f.
    // Against a black host background, 0.75 content alpha produces the same visual dim.
    private const val BackgroundPageAlpha = 0.75f

    private val animSpec =
            tween<IntOffset>(durationMillis = TransitionDuration, easing = CupertinoEasing)

    private val fadeSpec =
            tween<Float>(durationMillis = TransitionDuration, easing = CupertinoEasing)

    // ── Push (forward) ─────────────────────────────
    // New page slides in from the right (100%)
    val enterTransition: EnterTransition = slideInHorizontally(animationSpec = animSpec) { it }

    // The outgoing page moves left by ~25% of the screen width and dims against a black host background.
    val exitTransition: ExitTransition =
            slideOutHorizontally(animationSpec = animSpec) { -(it * SlideFactor).toInt() } +
                    fadeOut(animationSpec = fadeSpec, targetAlpha = BackgroundPageAlpha)

    // ── Pop (back) ──────────────────────────────
    // Underlying page slides back in from -25% position and undims back to full opacity.
    val popEnterTransition: EnterTransition =
            slideInHorizontally(animationSpec = animSpec) { -(it * SlideFactor).toInt() } +
                    fadeIn(animationSpec = fadeSpec, initialAlpha = BackgroundPageAlpha)

    // Top page slides out to the right (100%)
    val popExitTransition: ExitTransition = slideOutHorizontally(animationSpec = animSpec) { it }
}
