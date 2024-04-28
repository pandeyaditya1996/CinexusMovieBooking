import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {

  baseUrl = "http://alb-5dec01-407379133.us-west-1.elb.amazonaws.com:8000/";
  // baseUrl = "http://54.176.131.41:8000/"
  // baseUrl = "http://localhost:8000/";
  // baseUrl = "http://localhost:8000/";
  constructor(public httpClient: HttpClient) {

  }


  addStudent(body: any){
    return this.httpClient.post(this.baseUrl+'addStudent', body);
  }

  markAttendance(body: any){
    return this.httpClient.post(this.baseUrl+'mark-attendance', body);
  }

  startSession(courseId: any){
    return this.httpClient.post(this.baseUrl+'create-session', {courseId});
  }

  getSessionAttendees(sessionId: any){
    return this.httpClient.get(this.baseUrl+'attendances/session?sessionId='+sessionId);
  }

  getAttendanceData(courseId: any){
    return this.httpClient.get(this.baseUrl+'attendances?courseId='+courseId);
  }

  getStudents(courseId?: any){
    let appendString = "";
    if(courseId){
      appendString = '?courseId='+courseId;
    }
    return this.httpClient.get(this.baseUrl+'students'+appendString);
  }

  addMovie(body: any) {
    return this.httpClient.post(this.baseUrl+'admin/'+localStorage.getItem('userId')+'/add-movie/', body);
  }

  updateMovie(body: any, movieId: any) {
    return this.httpClient.post(this.baseUrl+'admin/'+localStorage.getItem('userId')+'/update-movie/'+movieId+'/', body);
  }

  addTheater(body: any) {
    return this.httpClient.post(this.baseUrl+'admin/'+localStorage.getItem('userId')+'/add-theater/', body);
  }

  updateTheater(body: any, theaterId: any) {
    return this.httpClient.post(this.baseUrl+'admin/'+localStorage.getItem('userId')+'/update-theater/'+theaterId+'/', body);
  }

  updateShowtime(body: any, showtimeId: any) {
    return this.httpClient.post(this.baseUrl+'admin/'+localStorage.getItem('userId')+'/update-showtime/'+showtimeId+'/', body);
  }

  getShowTime(){
    return this.httpClient.get(this.baseUrl+'showtimes/');
  }

  getCourses(professorId?: any){
    let appendString = "";
    if(professorId){
      appendString = "?professorId="+professorId;
    }
    return this.httpClient.get(this.baseUrl+'courses'+appendString);
  }

  addShowtime(body: any) {
    return this.httpClient.post(this.baseUrl+'admin/'+localStorage.getItem('userId')+'/add-showtime/', body);
  }

  enrollStudents(courseId: any, studentIds: any){
    return this.httpClient.post(this.baseUrl+'course/enroll-students?courseId='+courseId, {studentIds});
  }

  getCourseAttendance(courseId: any){
    return this.httpClient.get(this.baseUrl+'courseAttendance?courseId='+courseId);
  }

  getStudentAttendance(studentId: any){
    return this.httpClient.get(this.baseUrl+'studentAttendance?studentId='+studentId);
  }

  getAllSessions(courseId: any){
    return this.httpClient.get(this.baseUrl+'sessions?courseId='+courseId);
  }

  login(body: any) {
    return this.httpClient.post(this.baseUrl+'login/', body);
  }

  logout(email: any) {
    return this.httpClient.post(this.baseUrl+'logout/',{email});
  }

  getMovies(){
    return this.httpClient.get(this.baseUrl+'movies/');
  }
  getTheater(){
    return this.httpClient.get(this.baseUrl+'theaters/');
  }
  getShowtimes(){
    return this.httpClient.get(this.baseUrl+'showtimes/');
  }


  deleteTheater(theater_id: any) {
    return this.httpClient.delete(this.baseUrl+'admin/'+localStorage.getItem('userId')+'/delete-theater/'+theater_id);
  }
  deleteMovie(movie_id: any) {
    return this.httpClient.delete(this.baseUrl+'admin/'+localStorage.getItem('userId')+'/delete-movie/'+movie_id);
  }
  deleteShowtime(showtime_id: any) {
    return this.httpClient.delete(this.baseUrl+'admin/'+localStorage.getItem('userId')+'/delete-showtime/'+showtime_id);
  }

  getAnalyticsByLocation() {
    return this.httpClient.get(this.baseUrl+'admin/'+localStorage.getItem('userId')+'/analytics-by-location/');
  }

  getAnalyticsByMovie() {
    return this.httpClient.get(this.baseUrl+'admin/'+localStorage.getItem('userId')+'/analytics-by-movies/');
  }

}
