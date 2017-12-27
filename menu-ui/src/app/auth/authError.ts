export class AuthError {

  constructor(readonly context: string, readonly error: string, readonly errorDescription: string) {
  }

  public toString = (): string => {
    return this.context + ' ' + this.error + ' - ' + this.errorDescription;
  }

}
