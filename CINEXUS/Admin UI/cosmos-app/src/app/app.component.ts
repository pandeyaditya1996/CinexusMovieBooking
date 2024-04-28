import {Component, OnInit} from '@angular/core';
import { WebcamImage } from 'ngx-webcam';
import {Subject} from 'rxjs';
import {Observable} from 'rxjs';
import { AttendanceService } from './attendance.service';
import { Router } from '@angular/router';
@Component({
selector: 'app-root',
templateUrl: './app.component.html',
styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
    role: any;
    userId: any;
    localStorage: any;

    ngOnInit(): void {
        this.localStorage = localStorage;
        this.role = localStorage.getItem('role');
        this.userId = localStorage.getItem('userId');  
        if(!this.userId) {
            //this.router.navigate(['/login']);
        }
    }

    constructor(private attendanceService: AttendanceService, private router: Router){

    }

    public webcamImage: WebcamImage|null = null;
    handleImage(webcamImage: WebcamImage) {
        this.webcamImage = webcamImage;
    }
    logout(){
        this.localStorage.removeItem("userId");
        this.userId = null;
        this.router.navigate(['/login/']);
        // this.attendanceService.logout(localStorage.getItem('email')).subscribe(
        //     (res)=>{
        //         localStorage.removeItem('email');
        //         localStorage.removeItem('userId');
        //         localStorage.removeItem('role');
        //         this.router.navigate(['/login']);
        //     },
        //     (err)=>{

        //     }
        // )
    }

    
}