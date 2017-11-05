import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import * as moment from 'moment';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-prime-date-selector',
  templateUrl: './prime-date-selector.component.html',
  styleUrls: ['./prime-date-selector.component.scss']
})
export class PrimeDateSelectorComponent implements OnInit, OnChanges {
  @Input() date: moment.Moment;

  @Output() onUpdate = new EventEmitter<moment.Moment>();

  editableDate = new FormControl();
  myLocale: any;

  constructor() {
    this.myLocale = {
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

  ngOnChanges(changes: SimpleChanges): void {
    this.editableDate.setValue(this.date.toDate());
  }

  private onDateSelected(date: Date) {
    this.date = moment(date);
    this.onUpdate.emit(this.date);
  }

}
