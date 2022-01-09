/* eslint-disable no-prototype-builtins */
/* eslint-disable no-extra-boolean-cast */
import { Injectable } from '@angular/core';

/* const DEMO_COLLECTION = [
 *     { name: 'Alice', id: 1, age: 26 },
 *     { name: 'Bob', id: 2, age: 25 },
 *     { name: 'Jeremy', id: 4 , age: 26}
 * ];
 */
@Injectable()
export class UtilsHelperService {
  constructor() {}

  public isEmpty(value) {
    // Accept String, Object, Array, number
    if (value == null) {
      return true;
    }
    if (Array.isArray(value) || typeof value === 'string') {
      return !value.length;
    }
    for (const key in value) {
      if (Object.prototype.hasOwnProperty.call(value, key)) {
        return false;
      }
    }

    if (typeof value === 'object') {
      return Object.keys(value).length === 0;
    }
    return !!!value;
  }

  public isEqual(value1: string | number, value2: string | number) {
    // TODO: Need to add for array and Object
    return value1 === value2;
  }

  /* Creates a duplicate-free version of an array
   * uniqueBy(DEMO_COLLECTION, age) => [ { name: 'Bob', id: 2 }, { name: 'Jeremy', id: 4 , age: 26}]
   */
  public uniqBy(array: any[], identity: string): any[] {
    const [field1, field2] = identity.split('.');
    const filteredArr = array.reduce((accumalator, current) => {
      let has: boolean;
      if (this.isEmpty(field2)) {
        has = accumalator.some(item => item[field1] === current[field1]);
      } else {
        has = accumalator.some(item => item[field1][field2] === current[field1][field2]);
      }
      return has ? accumalator : [...accumalator, current];
    }, []);
    return filteredArr;
  }

  /* Group items by key
   * goupBy(DEMO_COLLECTION, 'age') => {26: [{ name: 'Alice', id: 1, age: 26 },{ name: 'Jeremy', id: 4 , age: 26}], 25: [{ name: 'Bob', id: 2, age: 25 }]}
   */
  public groupBy(collection: any[], iterate: string) {
    return collection.reduce((acc, value, index, a, key = this.getDeepObjectValue(value, iterate)) => {
      if (this.hasPropertyAndValue(value, iterate)) {
        acc[key] = acc[key] || [];
        acc[key].push(value);
      } else {
        acc[key] = [];
      }
      return acc;
    }, {});
  }

  /* partition list in two parts [0] => true [1] => false
   * partition(DEMO_COLLECTION, (item)=> item.age === 26) => [[{ name: 'Alice', id: 1, age: 26 },{ name: 'Jeremy', id: 4 , age: 26}], [{ name: 'Bob', id: 2, age: 25 }]]
   */
  public partition(collection, predicate): any[] {
    return collection.reduce(
      (acc, value) => {
        acc[predicate(value) ? 0 : 1].push(value);
        return acc;
      },
      [[], []]
    );
  }

  /* pluck value from list and return it
   * => pluck(DEMO_COLLECTION, name) =>  ["Alice", "Bob", "Jeremy"]
   */
  public pluck(collection: any[], field: string): any[] {
    const [field1, field2] = field.split('.');
    if (this.isEmpty(field2)) {
      return collection.map(item => item[field1]);
    }
    return collection.map(item => item[field1][field2]);
  }

  /*
   * Determine if the object has a property and value.working deep object
   * hasPropertyAndValue(Object, 'property1.property2.property3')
   */
  public hasPropertyAndValue(obj: Object, property: string): boolean {
    const field = property.split('.');
    for (let i = 0; i < field.length; i++) {
      if (this.isEmpty(obj) || !obj.hasOwnProperty(field[i]) || this.isEmpty(obj[field[i]])) {
        return false;
      }
      obj = obj[field[i]];
    }
    return true;
  }

  public getDeepObjectValue(obj: Object, property: string): any {
    const field = property.split('.');
    for (let i = 0; i < field.length; i++) {
      if (this.isEmpty(obj) || !obj.hasOwnProperty(field[i]) || this.isEmpty(obj[field[i]])) {
        return null;
      }
      obj = obj[field[i]];
    }
    return obj;
  }

  public uniqByWithoutField(array: any[]): [] {
    const filteredArr = array.reduce((accumalator, current) => {
      const has = accumalator.some(item => item === current);
      return has ? accumalator : [...accumalator, current];
    }, []);
    return filteredArr;
  }
}
