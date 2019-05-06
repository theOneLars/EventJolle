import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'top-navigation',
  templateUrl: './top-navigation.component.html',
  styleUrls: ['./top-navigation.component.css']
})
export class TopNavigationComponent implements OnInit {

  @Output() toggleSidbar = new EventEmitter();

  toggle() {
    this.toggleSidbar.emit();
  }

  ngOnInit(): void {
  }

}
