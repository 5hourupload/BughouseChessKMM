import SwiftUI
import shared

struct GameView: View {
    
    @ObservedObject var gm: GameManager

    @Binding var currentView: String
    

    var body: some View {
 
        let squareSize = UIScreen.main.bounds.width / 10
        
        ZStack()
        {
            VStack(spacing: 0) {
                Spacer()

                HStack(spacing: 0)
                {
                    TimerView(gameManager: gm, color: "white", boardNumber: 0)
                    Spacer()
                    StartButtonView(gameManager: gm)
                    Spacer()
                    TimerView(gameManager: gm, color: "black", boardNumber: 0)
                }.frame(width: squareSize*8)
                
                ZStack()
                {
                    HStack(spacing: 0)
                    {
                        VStack(spacing: 0) {
                            ForEach(0...4, id: \.self) { i in
                                CapturedSquareView(boardNumber: 0, gameManager: gm, square: gm.roster0W[i])
                            }
                        }
                        HStack(spacing: 0) {
                            ForEach((0...7), id: \.self) { y in
                                VStack(spacing: 0) {
                                    ForEach(0...7, id: \.self) { x in
                                        BoardSquareView(x: x, y: y, boardNumber: 0, gameManager: gm)
                                    }
                                }
                            }
                        }
                        VStack(spacing: 0) {
                            ForEach((0...4).reversed(), id: \.self) { i in
                                CapturedSquareView(boardNumber: 0, gameManager: gm, square: gm.roster0B[i])
                            }
                        }
                    }
                    HStack(spacing: 0)
                    {
                        VStack(spacing: 0) {
                            ForEach(0...4, id: \.self) { i in
                                CapturedPieceView(boardNumber: 0, gameManager: gm, square: gm.roster0W[i])
                            }
                        }.zIndex(10)
                        HStack(spacing: 0) {
                            ForEach((0...7), id: \.self) { y in
                                VStack(spacing: 0) {
                                    ForEach(0...7, id: \.self) { x in
                                        PieceView(x: x, y: y, boardNumber: 0, gameManager: gm)
                                    }
                                }
                            }
                        }
                        VStack(spacing: 0) {
                            ForEach((0...4).reversed(), id: \.self) { i in
                                CapturedPieceView(boardNumber: 0, gameManager: gm, square: gm.roster0B[i])
                            }
                        }
                    }
                    
                    PawnPromotionView(boardNumber: 0, gameManager: gm, show: gm.showPawnOptions[0])
                }
                
                Spacer()

                ZStack()
                {
                    HStack(spacing: 0) {
                        VStack(spacing: 0) {
                            ForEach(0...4, id: \.self) { i in
                                CapturedSquareView(boardNumber: 1, gameManager: gm, square: gm.roster1B[i])
                            }
                        }
                        
                        HStack(spacing: 0) {
                            ForEach((0...7).reversed(), id: \.self) { y in
                                VStack(spacing: 0) {
                                    ForEach((0...7).reversed(), id: \.self) { x in
                                        BoardSquareView(x: x, y: y, boardNumber: 1, gameManager: gm)
                                    }
                                }
                            }
                        }
                        VStack(spacing: 0) {
                            ForEach((0...4).reversed(), id: \.self) { i in
                                CapturedSquareView(boardNumber: 1, gameManager: gm, square: gm.roster1W[i])
                            }
                        }
                    }
                    HStack(spacing: 0) {
                        VStack(spacing: 0) {
                            ForEach(0...4, id: \.self) { i in
                                CapturedPieceView(boardNumber: 1, gameManager: gm, square: gm.roster1B[i])
                            }
                        }.zIndex(10)
                       
                        HStack(spacing: 0) {
                            ForEach((0...7).reversed(), id: \.self) { y in
                                VStack(spacing: 0) {
                                    ForEach((0...7).reversed(), id: \.self) { x in
                                        PieceView(x: x, y: y, boardNumber: 1, gameManager: gm)
                                    }
                                }
                            }
                        }
                        VStack(spacing: 0) {
                            ForEach((0...4).reversed(), id: \.self) { i in
                                CapturedPieceView(boardNumber: 1, gameManager: gm, square: gm.roster1W[i])
                            }
                        }
                                            
                    }
                    PawnPromotionView(boardNumber: 1, gameManager: gm, show: gm.showPawnOptions[1])
                }
                
                HStack(spacing: 0)
                {
                    TimerView(gameManager: gm, color: "black", boardNumber: 1)
                    Spacer()
                    Button("Options") {
                        currentView = "Options View"
                    }.frame(width: squareSize * 2, height: squareSize * 0.75).foregroundColor(.black)
                        .background(Color.gray)
                        .cornerRadius(10)
                    Spacer()
                    TimerView(gameManager: gm, color: "white", boardNumber: 1)
                    
                }.frame(width: squareSize*8)
                Spacer()

            }
            GameEndView(gameManager: gm, show: gm.showGameEndScreen)
        }

        

        }
    
    
    }

    
