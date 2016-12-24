import { InstantMessengerFrontendPage } from './app.po';

describe('client App', function() {
  let page: InstantMessengerFrontendPage;

  beforeEach(() => {
    page = new InstantMessengerFrontendPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
