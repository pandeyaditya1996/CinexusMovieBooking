import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AttendanceService } from '../attendance.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-professors',
  templateUrl: './professors.component.html',
  styleUrls: ['./professors.component.scss']
})
export class ProfessorsComponent implements OnInit {

  getProfessorSubscription: any;
  time = { hour: 13, minute: 30 };
  showtimes :any;
  showtimeId: any;
  edit = false;
  theaters: any;
  movies: any;
  showtime_subscription: any;

  showPopup = false;

  showtimeForm = new FormGroup({
    movie: new FormControl(),
    schedule_id: new FormControl(),
    theater: new FormControl(),
    movie_id: new FormControl(),
    theater_id: new FormControl(),
    showtime: new FormControl(),
    from_date: new FormControl(),
    to_date: new FormControl(),
    discount: new FormControl()
  });

  constructor(public attendanceService: AttendanceService, public router: Router){

  }
  ngOnInit(): void {
    this.showtime_subscription = this.attendanceService.getShowtimes().subscribe(
      (res: any)=>{
        this.showtimes = res;
      },
    (err : any)=>{
      this.showtimes =[{
        "theater": "AMC Kabuki 8",
        "schedule_id": 1,
        "movie": "Five Nights at Freddy's",
        "from_date": "2023-11-05",
        "to_date": "2023-12-05",
        "discount": "10.00",
        "seating_capacity": "100",
        "distance": 50.15,
        "address": "1881 Post St, San Francisco, CA 94115",
        "location_id": "5",
        "theater_id": 2,
        "location": "San Francisco",
        "showtime": "16:00:00"
      },
      {
        "name": "AMC Mercado 20",
        "theater": "AMC Kabuki 8",
        "schedule_id": 2,
        "movie": "The Marvels",
        "from_date": "2023-11-05",
        "to_date": "2023-12-05",
        "discount": "10.00",
        "seating_capacity": "100",
        "distance": 11.0,
        "address": "3111 Mission College Blvd, Santa Clara, CA 95054",
        "theater_id": 7,
        "location": "Santa Clara",
        "showtime": "18:00:00"
      },
      {
        "name": "Century Cinema 20",
        "theater": "AMC Kabuki 8",
        "schedule_id": 3,
        "movie": "The Marvels",
        "from_date": "2023-11-05",
        "to_date": "2023-12-05",
        "discount": "10.00",
        "seating_capacity": "100",
        "distance": 17.0,
        "address": "1800 S Shoreline Blvd, Sunnyvale, CA 94043",
        "theater_id": 12,
        "location": "Sunnyvale",
        "showtime": "15:00:00"
      },
      {
        "name": "INOX",
        "theater": "AMC Kabuki 8",
        "schedule_id": 4,
        "movie": "The Marvels",
        "from_date": "2023-11-05",
        "to_date": "2023-12-05",
        "discount": "10.00",
        "seating_capacity": "100",
        "distance": 5.2,
        "address": "754 the alameda,CA",
        "theater_id": 21,
        "location": "San Jose",
        "showtime": "15:30:00"
      }
    ]
    }
    )

    this.attendanceService.getTheater().subscribe(
      (res: any)=>{
        this.theaters = res;
      },
    (err: any)=>{
      // alert("");
      this.theaters = [
        {
          "theater_id": 2,
          "name": "AMC Kabuki 8"
        },
        {
          "theater_id": 7,
          "name": "AMC Mercado 20"
        },
        {
          "theater_id": 12,
          "name": "Century Cinema 20"
        },
        {
          "theater_id": 21,
          "name": "INOX"
        }
      ]
    }
    )

    this.attendanceService.getMovies().subscribe(
      (res: any)=>{
        this.movies = res;
      },
    (err: any)=>{
      // alert("");
      this.movies = [{
        "title": "Five Nights at Freddy's",
        "release_date": "2023-10-27" ,
        "duration": 109,
        "genre": "Horror",
        "language":"ENGLISH",
        "currently_running": 1,
        "movie_id": 1
      },
      {
        "title": "The Marvels",
        "release_date": "2023-11-10" ,
        "duration": 105,
        "genre": "Action",
        "language":"ENGLISH",
        "currently_running": 1,
        "movie_id": 2
      },
      {
        "title": "The Killer",
        "release_date": "2023-10-27" ,
        "duration": 118,
        "genre": "Action,Adventure",
        "language":"ENGLISH",
        "currently_running": 1,
        "movie_id": 3
      },
      {
        "title": "Napoleon",
        "release_date": "2023-12-22" ,
        "duration": 158,
        "genre": "Action,Adventure,Biography",
        "language":"ENGLISH",
        "currently_running": 1,
        "movie_id": 4
      },
      ]})
  
  
  }

  refreshShowtimes() {
    if(this.showtime_subscription) { this.showtime_subscription.unsubscribe();}
    this.showtime_subscription = this.attendanceService.getShowtimes().subscribe(
      (res: any)=>{
        this.showtimes = res;
      },(err)=>{
        this.showtimes = [{
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

    if(typeof this.showtimeForm.value.from_date === "object") {
      this.showtimeForm.value.from_date = this.showtimeForm?.value?.from_date['year']+"-"+this.makeTwoDigits(this.showtimeForm?.value?.from_date['month'])+"-"+this.makeTwoDigits(this.showtimeForm?.value?.from_date['day']);
    }
    if(typeof this.showtimeForm.value.to_date === "object") {
      this.showtimeForm.value.to_date = this.showtimeForm?.value?.to_date['year']+"-"+this.makeTwoDigits(this.showtimeForm?.value?.to_date['month'])+"-"+this.makeTwoDigits(this.showtimeForm?.value?.to_date['day']);
    }
    if(typeof this.showtimeForm.value.showtime === "object") {
      this.showtimeForm.value.showtime = this.makeTwoDigits(this.showtimeForm?.value?.showtime['hour'])+":"+this.makeTwoDigits(this.showtimeForm?.value?.showtime['minute'])+":"+this.makeTwoDigits(this.showtimeForm?.value?.showtime['second']);
    }

    if(!this.edit){
      this.attendanceService.addShowtime(this.showtimeForm.value).subscribe(
        (res)=>{
          alert("Showtime added successfully")
          this.refreshShowtimes();
          this.showPopup = false;
        },
        (err)=>{
          alert("Showtime addition failed")
        }
      )
    } else{
      this.attendanceService.updateShowtime(this.showtimeForm.value, this.showtimeId).subscribe(
        (res: any)=>{
          alert("Showtime updated successfully")
          this.refreshShowtimes();
          this.showPopup = false;
        },(err)=>{
          alert("Showtime updation failed")
        }
      )
    }
  } 

  makeTwoDigits(num:any) {
    num = ""+num;
    if(num.length == 1) {
      num = "0"+num;
    }
    return num;
  }

  closePopup(){
    this.showPopup = false;
    this.edit = false;
  }

  openAddProffesorPopup() {
    this.showPopup = true;
  }

  addShowtime() {
    if(typeof this.showtimeForm.value.showtime === "object") {
      this.showtimeForm.value.showtime = this.makeTwoDigits(this.showtimeForm?.value?.showtime['hour'] || "00")+":"+this.makeTwoDigits(this.showtimeForm?.value?.showtime['minute'] || "00")+":"+this.makeTwoDigits(this.showtimeForm?.value?.showtime['second'] || "00");
    }
    if(typeof this.showtimeForm.value.from_date === "object") {
      this.showtimeForm.value.from_date = this.showtimeForm?.value?.from_date['year']+"-"+this.makeTwoDigits(this.showtimeForm?.value?.from_date['month'])+"-"+this.makeTwoDigits(this.showtimeForm?.value?.from_date['day']);
    }
    if(typeof this.showtimeForm.value.to_date === "object") {
      this.showtimeForm.value.to_date = this.showtimeForm?.value?.to_date['year']+"-"+this.makeTwoDigits(this.showtimeForm?.value?.to_date['month'])+"-"+this.makeTwoDigits(this.showtimeForm?.value?.to_date['day']);
    }
    console.log(this.showtimeForm.value);
    this.attendanceService.addShowtime(this.showtimeForm.value).subscribe(
      (res)=>{
        alert("Showtime Added Successfully");
        // this.router.navigate(['/professors']);
        this.closePopup();
        //this.getProfessors();
      },
      (err)=>{

        alert("Error");
      }
    )
  }

  showUpdatePopup(showtime: any) {
    this.showPopup  =true;
    this.edit = true;
    this.showtimeForm.patchValue(showtime);
    this.showtimeId = showtime.schedule_id;
  }

  delShowtimes(item: any) {
    this.attendanceService.deleteShowtime(item.schedule_id).subscribe(
      (res: any)=>{
        alert("Deleted successfully")
        this.refreshShowtimes();
      },(err: any)=>{
        alert("Delete failed")
      }
    )
  }

}
