import * as focus from '../../../main/js/focus'
import { getFixture, clearFixture } from '../helpers/fixture'

describe('Focus', () => {
  let fixtureEl
  beforeAll(() => {
    fixtureEl = getFixture()
  })

  afterEach(() => {
    clearFixture()
  })

  it('should focus data-focus element', () => {
    fixtureEl.innerHTML = '<div id="parent"><input id="child1"></div><input id="child2" data-focus=""></div></div>'
    focus.focus(document)
    expect(document.activeElement).toEqual(document.getElementById("child2"))
  })

  it('should focus highest priority element', () => {
    fixtureEl.innerHTML = '<div id="parent"><input id="child1" data-focus="20"></div><input id="child2" data-focus="10"></div></div>'
    focus.focus(document)
    expect(document.activeElement).toEqual(document.getElementById("child1"))
  })

  it('should focus fallback priority to 0 if not parsable', () => {
    fixtureEl.innerHTML = '<div id="parent"><input id="child1" data-focus="nn"></div><input id="child2" data-focus="10"></div></div>'
    focus.focus(document)
    expect(document.activeElement).toEqual(document.getElementById("child2"))
  })

  it('should focus fallback priority to 0 if not parsable', () => {
    fixtureEl.innerHTML = '<div id="parent"><input id="child1"></div><input id="child2" data-focus="nn"></div></div>'
    focus.focus(document)
    expect(document.activeElement).toEqual(document.getElementById("child2"))
  })

  it('should gracefully fail if not data-focus', () => {
    fixtureEl.innerHTML = '<div id="parent"><input id="child1"></div><input id="child2"></div></div>'
    focus.focus(document)
    // by default, focus is on body
    expect(document.activeElement).toEqual(document.getElementsByTagName("body")[0])
  })

  it('should trigger on DOMContentLoaded event if installed', () => {
    fixtureEl.innerHTML = '<div id="parent"><input id="child1" data-focus></div><input id="child2"></div></div>'
    focus.install()
    let event = document.createEvent('Event');
    // by default, focus is on body
    expect(document.activeElement).toEqual(document.getElementsByTagName("body")[0])
    event.initEvent('DOMContentLoaded', true, true)
    window.document.dispatchEvent(event)
    expect(document.activeElement).toEqual(document.getElementById("child1"))
  })

  it('should search element inside element argument', () => {
    fixtureEl.innerHTML = '<div id="parent1"><input id="child0" data-focus="20"></div><div id="parent2"><input id="child1" data-focus="30"></div><input id="child2"></div></div>'
    focus.focus(document.getElementById("parent1"))
    expect(document.activeElement).toEqual(document.getElementById("child0"))
  })
  
  it('should not skip not empty field by default', () => {
    fixtureEl.innerHTML = '<div id="parent2"><input id="child1" data-focus value="not empty"></div><input id="child2" data-focus value=""></div></div>'
    focus.focus(document)
    expect(document.activeElement).toEqual(document.getElementById("child1"))
  })
  
  it('should skip not empty field', () => {
    fixtureEl.innerHTML = '<div id="parent2"><input id="child1" data-focus data-focus-skip="ifnotempty" value="not empty here"></div><input id="child2" data-focus value=""></div></div>'
    focus.focus(document)
    expect(document.activeElement).toEqual(document.getElementById("child2"))
  })
})
