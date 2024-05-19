import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { NgbNavModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [RouterModule, NgbNavModule],
  templateUrl: './navigation.component.html',
  styleUrl: './navigation.component.scss'
})
export class NavigationComponent {
  activeId: number = 1;

  constructor(private router: Router, private route: ActivatedRoute) {}

  ngOnInit() {
    this.setActiveTab(this.router.url);

    this.router.events.subscribe(() => {
      this.setActiveTab(this.router.url);
    });
  }

  private setActiveTab(url: string) {
    if (url.includes('/exchange-rates')) {
      this.activeId = 1;
    }

    if (url.includes('/calculator')) {
      this.activeId = 2;
    }

    if (url.includes('/currency')) {
      this.activeId = 1;
    }
  }
}
