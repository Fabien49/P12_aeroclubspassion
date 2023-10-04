import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRevision } from '../revision.model';

@Component({
  selector: 'jhi-revision-detail',
  templateUrl: './revision-detail.component.html',
})
export class RevisionDetailComponent implements OnInit {
  revision: IRevision | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ revision }) => {
      this.revision = revision;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
