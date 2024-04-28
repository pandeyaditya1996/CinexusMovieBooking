import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AttendanceService } from '../attendance.service';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.scss']
})
export class CoursesComponent implements OnInit{

  showPopup = false;
  theaters: any;
  theaterId: any;
  edit = false;
  theater_subscription: any;

  theaterForm = new FormGroup({
    name: new FormControl(''),
    location_id: new FormControl(''),
    seating_capacity: new FormControl(''),
    distance: new FormControl(''),
    coordinates: new FormControl(''),
    address: new FormControl('')
  });

  constructor(public router: Router, public attendanceService: AttendanceService){
    
  }
  ngOnInit(): void {
    this.attendanceService.getTheater().subscribe(
      (res: any)=>{
        this.theaters = res;
      },
    (err : any)=>{
      this.theaters =[{
        "name": "AMC Kabuki 8",
        "seating_capacity": "100",
        "distance": 50.15,
        "address": "1881 Post St, San Francisco, CA 94115",
        "location_id": "5",
        "theater_id": 1,
        "location": "San Francisco"
      },
      {
        "name": "AMC Mercado 20",
        "seating_capacity": "100",
        "distance": 11.0,
        "address": "3111 Mission College Blvd, Santa Clara, CA 95054",
        "theater_id": 2,
        "location": "Santa Clara"
      },
      {
        "name": "Century Cinema 20",
        "seating_capacity": "100",
        "distance": 17.0,
        "address": "1800 S Shoreline Blvd, Sunnyvale, CA 94043",
        "theater_id": 3,
        "location": "Sunnyvale"
      },
      {
        "name": "INOX",
        "seating_capacity": "100",
        "distance": 5.2,
        "address": "754 the alameda,CA",
        "theater_id": 4,
        "location": "San Jose"
      }
    ]
    }
    );
  }


  locations = [
    {
      "location_id": "1",
      "name": "San Francisco"
    },
    {
      "location_id": "2",
      "name": "San Jose"
    },
    {
      "location_id": "3",
      "name": "Santa Clara"
    },
    {
      "location_id": "4",
      "name": "Sunnyvale"
    },
    {
      "location_id": "5",
      "name": "Fremont"
    },
    {
      "location_id": "6",
      "name": "Cupertino"
    },
    {
      "location_id": "7",
      "name": "Milpitas"
    },
    {
      "location_id": "8",
      "name": "Palo Alto"
    },
    {
      "location_id": "9",
      "name": "Mountain View"
    }
  ]

  refreshTheaters() {
    if(this.theater_subscription) { this.theater_subscription.unsubscribe();}
    this.theater_subscription = this.attendanceService.getTheater().subscribe(
      (res: any)=>{
        this.theaters = res;
      },(err)=>{
        this.theaters = [{
          "title": "Five Nights at Freddy's",
          "release_date": "2023-10-27" ,
          "duration": 109,
          "genre": "Horror",
          "language":"ENGLISH",
          "currently_running": 1
        },
        {
          "title": "The Marvels",
          "release_date": "2023-11-10" ,
          "duration": 105,
          "genre": "Action",
          "language":"ENGLISH",
          "currently_running": 1
        },
        {
          "title": "The Killer",
          "release_date": "2023-10-27" ,
          "duration": 118,
          "genre": "Action,Adventure",
          "language":"ENGLISH",
          "currently_running": 1
        },
        {
          "title": "Napoleon",
          "release_date": "2023-12-22" ,
          "duration": 158,
          "genre": "Action,Adventure,Biography",
          "language":"ENGLISH",
          "currently_running": 1
        },
      ]
      }
    )
  }

  onSubmit(){
    if(!this.edit){
      this.attendanceService.addTheater(this.theaterForm.value).subscribe(
        (res)=>{
          alert("Theater added successfully")
          this.refreshTheaters();
          this.showPopup = false;
        },
        (err)=>{
          alert("Theater addition failed")
        }
      )
    } else{
      this.attendanceService.updateTheater(this.theaterForm.value, this.theaterId).subscribe(
        (res: any)=>{
          alert("Theater updated successfully")
          this.refreshTheaters();
          this.showPopup = false;
        },(err)=>{
          alert("Theater updation failed")
        }
      )
    }
  } 

  closePopup(){

    this.showPopup = false;
    this.edit = false;
  }

  openPopup() {
    this.showPopup = true;
  }

  startSession(code: any) {

    this.attendanceService.startSession(code).subscribe(
      (res: any)=>{
        this.router.navigate(['/mark/'+res.sessionId],{queryParams: {courseId:code}});
      },(err)=>{

      }
    )
  }

  viewCourseAttendance(code: any, name: any) {
    
    this.router.navigate(['view-attendance/'+code], {queryParams: {name:name}});
  }

  getCourses(){
    this.attendanceService.getCourses().subscribe(
      (res: any)=>{
        this.theaters = res;
      },(err)=>{
        // alert("error");
      }
    )

  }
  
  showUpdatePopup(theater: any) {
    this.showPopup  =true;
    this.edit = true;
    this.theaterForm.patchValue(theater);
    this.theaterId = theater.theater_id;
  }

  delTheater(item: any) {
    this.attendanceService.deleteTheater(item.theater_id).subscribe(
      (res: any)=>{
        alert("Deleted successfully")
        this.refreshTheaters();
      },(err: any)=>{
        alert("Delete failed")
      }
    )
  }

}
