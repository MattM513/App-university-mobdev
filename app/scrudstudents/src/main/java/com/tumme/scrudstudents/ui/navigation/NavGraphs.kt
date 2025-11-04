package com.tumme.scrudstudents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.tumme.scrudstudents.ui.auth.AuthViewModel
import com.tumme.scrudstudents.ui.auth.LoginScreen
import com.tumme.scrudstudents.ui.auth.RegisterScreen
import com.tumme.scrudstudents.ui.auth.UserRole
import com.tumme.scrudstudents.ui.course.CourseFormScreen
import com.tumme.scrudstudents.ui.course.CourseListScreen
import com.tumme.scrudstudents.ui.student.StudentGradesScreen
import com.tumme.scrudstudents.ui.student.StudentHomeScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeFormScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeListScreen
import com.tumme.scrudstudents.ui.teacher.TeacherHomeScreen

object Routes {
    const val AUTH_GRAPH = "auth_graph"
    const val LOGIN_SCREEN = "login"
    const val REGISTER_SCREEN = "register"

    const val STUDENT_GRAPH = "student_graph"
    const val STUDENT_HOME = "student_home"
    const val STUDENT_COURSES = "student_courses/{level}"
    const val STUDENT_SUBSCRIBE = "student_subscribe/{studentId}"
    const val STUDENT_GRADES = "student_grades/{studentId}"
    const val STUDENT_FINAL_GRADE = "student_final_grade/{studentId}"

    const val TEACHER_GRAPH = "teacher_graph"
    const val TEACHER_HOME = "teacher_home"
    const val TEACHER_COURSES = "teacher_courses/{teacherId}"
    const val TEACHER_COURSE_FORM = "teacher_course_form/{teacherId}"
    const val TEACHER_GRADE_ENTRY = "teacher_grade_entry/{teacherId}"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    val startDestination = if (authState.isAuthenticated) {
        when (authState.role) {
            UserRole.STUDENT -> Routes.STUDENT_GRAPH
            UserRole.TEACHER -> Routes.TEACHER_GRAPH
            else -> Routes.AUTH_GRAPH
        }
    } else {
        Routes.AUTH_GRAPH
    }

    LaunchedEffect(authState) {
        if (!authState.isAuthenticated && navController.currentDestination?.parent?.route != Routes.AUTH_GRAPH) {
            navController.navigate(Routes.AUTH_GRAPH) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(navController, startDestination = startDestination) {
        authGraph(navController, authViewModel)
        studentGraph(navController, authViewModel)
        teacherGraph(navController, authViewModel)
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    navigation(startDestination = Routes.LOGIN_SCREEN, route = Routes.AUTH_GRAPH) {
        composable(Routes.LOGIN_SCREEN) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER_SCREEN)
                },
                onLoginSuccess = { role, _ ->
                    val route = when (role) {
                        UserRole.STUDENT -> Routes.STUDENT_GRAPH
                        UserRole.TEACHER -> Routes.TEACHER_GRAPH
                        else -> Routes.LOGIN_SCREEN
                    }
                    navController.navigate(route) {
                        popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.REGISTER_SCREEN) {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = { role, _ ->
                    val route = when (role) {
                        UserRole.STUDENT -> Routes.STUDENT_GRAPH
                        UserRole.TEACHER -> Routes.TEACHER_GRAPH
                        else -> Routes.LOGIN_SCREEN
                    }
                    navController.navigate(route) {
                        popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                    }
                }
            )
        }
    }
}

private fun NavGraphBuilder.studentGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    navigation(startDestination = Routes.STUDENT_HOME, route = Routes.STUDENT_GRAPH) {
        composable(Routes.STUDENT_HOME) {
            val studentId = authViewModel.authState.collectAsState().value.userId
            StudentHomeScreen(
                studentId = studentId,
                onNavigateToCourses = { level ->
                    navController.navigate(Routes.STUDENT_COURSES.replace("{level}", level))
                },
                onNavigateToEnroll = {
                    navController.navigate(Routes.STUDENT_SUBSCRIBE.replace("{studentId}", studentId.toString()))
                },
                onNavigateToMyGrades = {
                    navController.navigate(Routes.STUDENT_GRADES.replace("{studentId}", studentId.toString()))
                },
                onNavigateToFinalGrade = {
                    navController.navigate(Routes.STUDENT_FINAL_GRADE.replace("{studentId}", studentId.toString()))
                },
                onLogout = {
                    authViewModel.logout()
                }
            )
        }

        composable(
            route = Routes.STUDENT_COURSES,
            arguments = listOf(navArgument("level") { type = NavType.StringType })
        ) { backStackEntry ->
            val level = backStackEntry.arguments?.getString("level") ?: ""
            CourseListScreen(
                filterLevel = level,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.STUDENT_SUBSCRIBE,
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
            SubscribeFormScreen(
                studentId = studentId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.STUDENT_GRADES,
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
            SubscribeListScreen(
                studentId = studentId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.STUDENT_FINAL_GRADE,
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
            StudentGradesScreen(
                studentId = studentId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

private fun NavGraphBuilder.teacherGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    navigation(startDestination = Routes.TEACHER_HOME, route = Routes.TEACHER_GRAPH) {
        composable(Routes.TEACHER_HOME) {
            val teacherId = authViewModel.authState.collectAsState().value.userId
            TeacherHomeScreen(
                teacherId = teacherId,
                onNavigateToCourses = {
                    navController.navigate(Routes.TEACHER_COURSES.replace("{teacherId}", teacherId.toString()))
                },
                onNavigateToGradeEntry = {
                    navController.navigate(Routes.TEACHER_GRADE_ENTRY.replace("{teacherId}", teacherId.toString()))
                },
                onLogout = {
                    authViewModel.logout()
                }
            )
        }

        composable(
            route = Routes.TEACHER_COURSES,
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) { backStackEntry ->
            val teacherId = backStackEntry.arguments?.getInt("teacherId") ?: 0
            CourseListScreen(
                teacherId = teacherId,
                onNavigateToForm = {
                    navController.navigate(Routes.TEACHER_COURSE_FORM.replace("{teacherId}", teacherId.toString()))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.TEACHER_COURSE_FORM,
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) { backStackEntry ->
            val teacherId = backStackEntry.arguments?.getInt("teacherId") ?: 0
            CourseFormScreen(
                teacherId = teacherId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.TEACHER_GRADE_ENTRY,
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) {
            SubscribeFormScreen(
                teacherId = authViewModel.authState.collectAsState().value.userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}