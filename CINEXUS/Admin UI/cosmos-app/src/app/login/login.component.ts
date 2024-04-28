import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AttendanceService } from '../attendance.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Router } from '@angular/router';
// import * as jwt_decode from "jwt-decode";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  loginForm= new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  });

  userBody: any;
  role: any;
  userId: any;

  constructor(public attendanceService: AttendanceService, private jwtHelper: JwtHelperService, private router: Router){

  }

  onSubmit(){
    this.attendanceService.login(this.loginForm.value).subscribe(
      (res: any)=>{
        this.userBody = this.jwtHelper.decodeToken(res?.accessToken);
        console.log(this.userBody);
        this.role = res.role;
        this.userId = res.user_id;

        localStorage.setItem('role', this.role);
        localStorage.setItem('userId', this.userId);
        localStorage.setItem('username', this.loginForm?.value?.username ? this.loginForm?.value?.username : '' );

        this.router.navigate(['/movies/']);

      },(err)=>{

      }
    )
  }

  clear(){

  }

}
