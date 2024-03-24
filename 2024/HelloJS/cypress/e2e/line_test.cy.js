describe('Create a line', () => {
  it('passes', () => {
    cy.visit("http://localhost:5500")

    // create two nodes
    cy.get('#hMainCanvas').click(100,100)
    cy.get('#hMainCanvas').click(300,300)

    cy.get('#hMainCanvas')
      .trigger('mousedown', 100, 100, {which: 0})
      .trigger('mousemove', 300, 300)
      .trigger('mouseup', 300,300,{ which: 0 })


  })
})