import {inject, Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root',
})
export class OntologyService {

  private readonly http = inject(HttpClient);

  public getClassList(): Observable<string[]> {
    return this.http.get<string[]>('/api/ontology/classlist');
  }

  public getInstancesByClass(selectedClass: string): Observable<string[]> {
    return this.http.get<string[]>(`/api/ontology/instances/${selectedClass}`);
  }
}
