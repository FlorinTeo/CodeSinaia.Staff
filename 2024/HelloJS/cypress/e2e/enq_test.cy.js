describe('Enqueue and Dequeue two nodes', () => {
  it('passes', () => {
    cy.visit("http://localhost:5500")
    
    // create two nodes
    cy.get('#hMainCanvas').click(100,100)
    cy.get('#hMainCanvas').click(300,300)

    cy.get('#hMainCanvas').rightclick(100,100)
    cy.contains("Enqueue").click()

    cy.get('#hMainCanvas').rightclick(300,300)
    cy.contains("Enqueue").click()

    cy.get('#hMainCanvas').rightclick(100,100)
    cy.contains("Dequeue").click()

    cy.get('#hMainCanvas').rightclick(300,300)
    cy.contains("Dequeue").click()

  })
})