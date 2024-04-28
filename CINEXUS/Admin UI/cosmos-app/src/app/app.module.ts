import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { WebcamModule } from 'ngx-webcam';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CameraComponent } from './camera/camera.component';
import { AddStudentComponent } from './add-student/add-student.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MarkAttendanceComponent } from './mark-attendance/mark-attendance.component';
import { CameraPopupComponent } from './camera-popup/camera-popup.component';
import { HttpClientModule } from  '@angular/common/http';
import { StudentsComponent } from './students/students.component';
import { ProfessorsComponent } from './professors/professors.component';
import { CoursesComponent } from './courses/courses.component';
import { AddCourseComponent } from './add-course/add-course.component';
import { ViewAttendanceComponent } from './view-attendance/view-attendance.component';
import { NgbAccordionModule, NgbModule, NgbTimepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { JwtModule } from '@auth0/angular-jwt';
import { AnalyticsComponent } from './analytics/analytics.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    AppComponent,
    CameraComponent,
    AddStudentComponent,
    MarkAttendanceComponent,
    CameraPopupComponent,
    StudentsComponent,
    ProfessorsComponent,
    CoursesComponent,
    AddCourseComponent,
    ViewAttendanceComponent,
    LoginComponent,
    DashboardComponent,
    AnalyticsComponent,
  ],
  imports: [
    BrowserModule,
    BrowserModule,
    AppRoutingModule,
    WebcamModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    NgbAccordionModule,
    FormsModule,
    NgbTimepickerModule,
    JwtModule.forRoot({
      config: {
        tokenGetter:  () => localStorage.getItem('access_token')
      }
    }),
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
