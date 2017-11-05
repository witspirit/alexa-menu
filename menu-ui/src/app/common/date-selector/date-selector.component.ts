import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import * as moment from 'moment';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-date-selector',
  templateUrl: './date-selector.component.html',
  styleUrls: ['./date-selector.component.css']
})
export class DateSelectorComponent implements OnInit, OnChanges {
  @Input() date: moment.Moment;
  @Input() jumpLength: number; // in days

  @Output() onUpdate = new EventEmitter<moment.Moment>();

  selectedDate = new FormControl();
  selectedJumpLength = new FormControl();

  constructor() {
  }

  ngOnInit() {
  }

  public applyDateUpdate() {
    const parsedDate = moment(this.selectedDate.value, 'YYYYMMDD', true);
    console.log('Parsed date : ' + parsedDate);

    if (parsedDate.isValid() && !parsedDate.isSame(this.date)) {
      this.update(parsedDate);
    } else {
      console.log('Parsing flags: ' + JSON.stringify(parsedDate.parsingFlags()));
    }
  }

  // Probably can do something smarter using some binding approach
  public applyJumpLengthUpdate() {
    this.jumpLength = this.selectedJumpLength.value;
    // Not emitting this, as the value is really only useful internally.
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.selectedDate.setValue(this.date.format('YYYYMMDD'));
    this.selectedJumpLength.setValue(this.jumpLength);
  }

  public earlier() {
    this.update(moment(this.date).subtract(this.jumpLength, 'days'));
  }

  public later() {
    this.update(moment(this.date).add(this.jumpLength, 'days'));
  }

  private update(desiredDate: moment.Moment) {
    this.onUpdate.emit(desiredDate);
  }

}
