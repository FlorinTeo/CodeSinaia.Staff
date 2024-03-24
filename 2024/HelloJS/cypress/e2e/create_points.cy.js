describe('Create multiple nodes', () => {
  it('passes', () => {
    cy.visit("http://localhost:5500")
    
    // create a bunch of nodes
    cy.get('#hMainCanvas').click(100,100)
    cy.get('#hMainCanvas').click(300,300)
    cy.get('#hMainCanvas').click(100,300)
    cy.get('#hMainCanvas').click(300,100)
    cy.get('#hMainCanvas').click(400,400)


    

  })
})