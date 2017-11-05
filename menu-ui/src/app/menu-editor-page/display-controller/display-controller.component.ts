import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import * as moment from 'moment';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-display-controller',
  templateUrl: './display-controller.component.html',
  styleUrls: ['./display-controller.component.scss']
})
export class DisplayControllerComponent implements OnInit, OnChanges {
  @Input() date: moment.Moment;
  @Input() jumpLength: number; // in days
  @Input() nrOfDays: number; // in days

  @Output() onStartDateUpdate = new EventEmitter<moment.Moment>();
  @Output() onNrOfDaysUpdate = new EventEmitter<number>();

  selectedNrOfDays = new FormControl();

  editableDate = new FormControl();
  myDateLocale: any;

  constructor() {
    this.myDateLocale = {
      firstDayOfWeek: 1,
      dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
      dayNamesShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
      dayNamesMin: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
      monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
      monthNamesShort: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
      today: 'Today',
      clear: 'Clear'
    };
  }

  ngOnInit() {
  }

  public applyNrOfDaysUpdate() {
    this.nrOfDays = this.selectedNrOfDays.value;
    this.onNrOfDaysUpdate.emit(this.nrOfDays);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.selectedNrOfDays.setValue(this.nrOfDays);
    this.editableDate.setValue(this.date.toDate());
  }

  private onDateSelected(date: Date) {
    this.date = moment(date);
    this.update(this.date);
  }

  private update(desiredDate: moment.Moment) {
    this.onStartDateUpdate.emit(desiredDate);
  }

}
