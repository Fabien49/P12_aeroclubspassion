import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRevision } from '../revision.model';
import { RevisionService } from '../service/revision.service';
import { RevisionDeleteDialogComponent } from '../delete/revision-delete-dialog.component';

@Component({
  selector: 'jhi-revision',
  templateUrl: './revision.component.html',
})
export class RevisionComponent implements OnInit {
  revisions?: IRevision[];
  isLoading = false;

  constructor(protected revisionService: RevisionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.revisionService.query().subscribe(
      (res: HttpResponse<IRevision[]>) => {
        this.isLoading = false;
        this.revisions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IRevision): number {
    return item.id!;
  }

  delete(revision: IRevision): void {
    const modalRef = this.modalService.open(RevisionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.revision = revision;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
