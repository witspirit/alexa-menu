import { MenuUiPage } from './app.po';

describe('menu-ui App', () => {
  let page: MenuUiPage;

  beforeEach(() => {
    page = new MenuUiPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});
