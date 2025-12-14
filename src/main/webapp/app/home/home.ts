import {Component, OnDestroy, OnInit, inject, signal, ChangeDetectorRef} from '@angular/core';
import { Router } from '@angular/router';

import { Subject } from 'rxjs';

import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import SharedModule from 'app/shared/shared.module';
import {OntologyService} from "../core/util/ontology.service";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'jhi-home',
  templateUrl: './home.html',
  styleUrl: './home.scss',
  imports: [SharedModule, FormsModule],
})
export default class Home implements OnInit, OnDestroy {
  account = signal<Account | null>(null);
  classList: string[] = [];
  selectedClass: string = '';
  instances: string[] = [];

  private readonly destroy$ = new Subject<void>();

  private readonly accountService = inject(AccountService);
  private readonly ontologyService = inject(OntologyService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly router = inject(Router);


  ngOnInit(): void {
    // this.accountService
    //   .getAuthenticationState()
    //   .pipe(takeUntil(this.destroy$))
    //   .subscribe(account => this.account.set(account));

    this.ontologyService.getClassList().subscribe({
      next: (res) => {
        this.classList = res;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error(error)
      }
    });
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadInstances() {
    this.instances = [];
    this.ontologyService.getInstancesByClass(this.selectedClass).subscribe({
      next: (res) => {
        this.instances = res;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error(error)
      }
    });
  }
}
