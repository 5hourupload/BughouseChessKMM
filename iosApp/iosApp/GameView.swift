import SwiftUI
import shared

struct GameView: View {
    @ObservedObject var gameManager: GameManager

    init() {
        self.gameManager =  GameManager(counter: 0)
    }

    var body: some View {
 
        let squareSize = UIScreen.main.bounds.width / 10

        VStack(spacing: 10) {
            
            HStack(spacing: 0) {
                
                ScrollView {
                    VStack(spacing: 0) {
                        ForEach(0...29, id: \.self) { i in
                            RosterSquareView(i: i, boardNumber: 0, color: "white", gameManager: gameManager, square: gameManager.roster0W[i]).onTapGesture {
                                
                            }
                        }
                    }
                }
                .frame(height: squareSize * 8)
                
                HStack(spacing: 0) {
                    ForEach((0...7), id: \.self) { y in
                        VStack(spacing: 0) {
                            ForEach(0...7, id: \.self) { x in
                                BoardSquareView(x: x, y: y, boardNumber: 0, gameManager: gameManager).onTapGesture {
                                    gameManager.processMove(x: x,y: y,boardNumber: 0)
                                }
                            }
                        }
                    }
                }
                
                ScrollView {
                    VStack(spacing: 0) {
                        ForEach(0...29, id: \.self) { i in
                            RosterSquareView(i: i, boardNumber: 0, color: "black", gameManager: gameManager, square: gameManager.roster0B[i]).onTapGesture {
//                                gameManager.processMove(x: x,y: y,boardNumber: 0)
                            }
                        }
                    }
                }
                .frame(height: squareSize * 8)
                
            }

            
            HStack(spacing: 0) {
                
                ScrollView {
                    VStack(spacing: 0) {
                        ForEach(0...29, id: \.self) { i in
                            RosterSquareView(i: i, boardNumber: 1, color: "black", gameManager: gameManager, square: gameManager.roster1B[i]).onTapGesture {
//                                gameManager.processMove(x: x,y: y,boardNumber: 0)
                            }
                        }
                    }
                }
                .frame(height: squareSize * 8)
                
                HStack(spacing: 0) {
                    ForEach((0...7).reversed(), id: \.self) { y in
                        VStack(spacing: 0) {
                            ForEach((0...7).reversed(), id: \.self) { x in
                                BoardSquareView(x: x, y: y, boardNumber: 1, gameManager: gameManager).onTapGesture {
                                    gameManager.processMove(x: x,y: y,boardNumber: 1)
                                }
                            }
                        }
                    }
                }
                
                ScrollView {
                    VStack(spacing: 0) {
                        ForEach(0...29, id: \.self) { i in
                            RosterSquareView(i: i, boardNumber: 1, color: "white", gameManager: gameManager, square: gameManager.roster1W[i]).onTapGesture {
//                                gameManager.processMove(x: x,y: y,boardNumber: 0)
                            }
                        }
                    }
                }
                .frame(height: squareSize * 8)
            }

            
            
        }
        
        
        

        }
    }

    
