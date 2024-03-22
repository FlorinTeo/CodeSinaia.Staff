
describe('Create a line', () => {
  it('passes', () => {
    cy.visit("http://localhost:3001")

    // create two nodes
    cy.get('#hMainCanvas').click(100,100)
    cy.get('#hMainCanvas').click(300,300)
    

    cy.get('#hMainCanvas')
      .trigger('mousedown', 100, 100, { which: 0 })
      .trigger('mousemove', 300, 300, { which: 1 })
      .trigger('mouseup', 300, 300, { which: 1 })

    // cy.get('#hMainCanvas').click(100,100, {release:false})
    // cy.get('#hMainCanvas').click(300,300, {release:true})

  })
})

// describe('Enqueue and Dequeue two nodes', () => {
//   it('passes', () => {
//     cy.visit("http://localhost:3001")
    
//     // create two nodes
//     cy.get('#hMainCanvas').click(100,100)
//     cy.get('#hMainCanvas').click(300,300)

//     cy.get('#hMainCanvas').rightclick(100,100)
//     cy.contains("Enqueue").click()

//     cy.get('#hMainCanvas').rightclick(300,300)
//     cy.contains("Enqueue").click()

//     cy.get('#hMainCanvas').rightclick(100,100)
//     cy.contains("Dequeue").click()

//     cy.get('#hMainCanvas').rightclick(300,300)
//     cy.contains("Dequeue").click()

//   })
// })