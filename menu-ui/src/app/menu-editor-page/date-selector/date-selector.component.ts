import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import * as moment from 'moment';
import { FormControl } from '@angular/forms';

const jumpLength = 1;

const RETURN_KEY = 13;

@Component({
  selector: 'app-date-selector',
  templateUrl: './date-selector.component.html',
  styleUrls: ['./date-selector.component.css']
})
export class DateSelectorComponent implements OnInit, OnChanges {
  @Input() date: moment.Moment;
  @Output() onUpdate = new EventEmitter<moment.Moment>();

  selectedDate = new FormControl();

  constructor() {
  }

  ngOnInit() {
  }

  public handleUpdate(event: KeyboardEvent) {
    if (event.keyCode === RETURN_KEY) {
      const parsedDate = moment(this.selectedDate.value, 'YYYYMMDD', true);
      console.log('Parsed date : ' + parsedDate);

      if (parsedDate.isValid() && !parsedDate.isSame(this.date)) {
        this.update(parsedDate);
      } else {
        console.log('Parsing flags: ' + JSON.stringify(parsedDate.parsingFlags()));
      }
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.selectedDate.setValue(this.date.format('YYYYMMDD'));
  }

  public earlier() {
    this.update(moment(this.date).subtract(jumpLength, 'days'));
  }

  public later() {
    this.update(moment(this.date).add(jumpLength, 'days'));
  }

  private update(desiredDate: moment.Moment) {
    this.onUpdate.emit(desiredDate);
  }

}
