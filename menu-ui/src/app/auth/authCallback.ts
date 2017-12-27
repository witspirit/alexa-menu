export class AuthCallback {
  constructor(readonly onSuccess: (User) => void, readonly onError: (AuthError) => void) {
  }
}
