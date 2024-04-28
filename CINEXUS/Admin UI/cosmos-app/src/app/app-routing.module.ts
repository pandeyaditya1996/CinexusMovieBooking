import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MarkAttendanceComponent } from './mark-attendance/mark-attendance.component';
import { AppComponent } from './app.component';
import { AddStudentComponent } from './add-student/add-student.component';
import { StudentsComponent } from './students/students.component';
import { CoursesComponent } from './courses/courses.component';
import { ProfessorsComponent } from './professors/professors.component';
import { AddCourseComponent } from './add-course/add-course.component';
import { ViewAttendanceComponent } from './view-attendance/view-attendance.component';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AnalyticsComponent } from './analytics/analytics.component';

const routes: Routes = [

  {
    path: 'movies',
    component: StudentsComponent,
  },
  {
    path: 'showtimes',
    component: ProfessorsComponent,
  },
  {
    path: 'theaters',
    component: CoursesComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'analytics',
    component: AnalyticsComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
