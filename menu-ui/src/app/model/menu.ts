export class Menu {
  constructor(public date: string, public dinner: string) {
  }

  // Using arrow function to ensure this is preserved
  public toString = (): string => {
    return this.date + ': ' + this.dinner;
  }
}
