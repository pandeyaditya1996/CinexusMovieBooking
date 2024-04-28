import { Component, OnInit } from '@angular/core';
import { AttendanceService } from '../attendance.service';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
  styleUrls: ['./students.component.scss']
})
export class StudentsComponent implements OnInit{

  showPopup = false;
  edit = false;
  movieId: any;

  movieForm = new FormGroup({
    title: new FormControl(''),
    description: new FormControl(''),
    release_date: new FormControl(),
    duration: new FormControl(''),
    genre: new FormControl(''),
    currently_running: new FormControl()
  });

  movies: any;
  movie_subscription: any;

// running = [
//   {
//     ""
//   }
// ]

  constructor(public attendanceService: AttendanceService){

  }

  getFormattedDate(date: any){
    const dateSplit = date.split('-');
    date = {
      year: dateSplit[0],
      month: dateSplit[1],
      day: dateSplit[2]
    }
  }


  ngOnInit(): void {
    this.movie_subscription = this.attendanceService.getMovies().subscribe(
      (res: any)=>{
        this.movies = res;
      },(err)=>{
        this.movies = [{
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

  refreshMovies() {
    if(this.movie_subscription) { this.movie_subscription.unsubscribe();}
    this.movie_subscription = this.attendanceService.getMovies().subscribe(
      (res: any)=>{
        this.movies = res;
      },(err)=>{
        this.movies = [{
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

  closePopup(){
    this.edit = false;
    this.showPopup = false;
  }

  makeTwoDigits(num:any) {
    num = ""+num;
    if(num.length == 1) {
      num = "0"+num;
    }
    return num;
  }

  onSubmit(){
    // this.addProfessor();
    if(typeof this.movieForm.value.release_date === "object") {
      this.movieForm.value.release_date = this.movieForm?.value?.release_date['year']+"-"+this.makeTwoDigits(this.movieForm?.value?.release_date['month'])+"-"+this.makeTwoDigits(this.movieForm?.value?.release_date['day']);
    }
    if(!this.edit){
            
      this.attendanceService.addMovie(this.movieForm.value).subscribe(
        (res: any)=>{
          alert("Movie added successfully")
          this.refreshMovies();
          this.showPopup = false;
        },(err)=>{
          alert("Movie addition failed")
        }
      )
    } else{
      this.attendanceService.updateMovie(this.movieForm.value, this.movieId).subscribe(
        (res: any)=>{
          alert("Movie updated successfully")
          this.refreshMovies();
          this.showPopup = false;
        },(err)=>{
          alert("Movie updation failed")
        }
      )
    }


  }

  joinDate(dateSplit: any): string {
    return `${dateSplit.year}-${dateSplit.month}-${dateSplit.day}`;
  }
  

  showUpdatePopup(movie: any){
    this.showPopup = true;
    const tempMovie = JSON.parse(JSON.stringify(movie));
    const movieSplit = tempMovie['release_date'].split("-");
    tempMovie['release_date'] = {year: movieSplit[0], month:movieSplit[1], day: movieSplit[2]};
    this.movieForm.patchValue(tempMovie);
    this.edit = true;
    this.movieId = movie.movie_id;
  }
  delMovies(item: any) {
    this.attendanceService.deleteMovie(item.movie_id).subscribe(
      (res: any)=>{
        alert("Deleted successfully")
        this.refreshMovies();
      },(err: any)=>{
        alert("Delete failed")
      }
    )
  }


}
