import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import * as moment from 'moment';

const jumpLength = 1;

@Component({
  selector: 'app-date-selector',
  templateUrl: './date-selector.component.html',
  styleUrls: ['./date-selector.component.css']
})
export class DateSelectorComponent implements OnInit, OnChanges {
  @Input() date: moment.Moment;
  @Output() onUpdate = new EventEmitter<moment.Moment>();

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
  }

  public earlier() {
    this.onUpdate.emit(moment(this.date).subtract(jumpLength, 'days'));
  }

  public later() {
    this.onUpdate.emit(moment(this.date).add(jumpLength, 'days'));
  }

}
