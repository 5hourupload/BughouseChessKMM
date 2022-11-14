import SwiftUI
import shared

struct GameView: View {
    @ObservedObject var gm: GameManager

    init() {
        self.gm = GameManager(counter: 0)
    }

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
                
                HStack(spacing: 0) {
                    
//                    ScrollView {
//                        VStack(spacing: 0) {
//                            ForEach(0...29, id: \.self) { i in
//                                RosterSquareView(i: i, boardNumber: 0, color: "white", gameManager: gm, square: gm.roster0W[i]).onTapGesture {
//                                    gm.processRosterClick(i: i, roster: gm.roster0W, boardNumber: 0)
//                                }
//                            }
//                        }
//                    }
//                    .frame(height: squareSize * 8)
                    
                    ZStack()
                    {
                        HStack(spacing: 0) {
                            ForEach((0...7), id: \.self) { y in
                                VStack(spacing: 0) {
                                    ForEach(0...7, id: \.self) { x in
                                        BoardSquareView(x: x, y: y, boardNumber: 0, gameManager: gm)
                                    }
                                }
                            }
                        }
                        HStack(spacing: 0) {
                            ForEach((0...7), id: \.self) { y in
                                VStack(spacing: 0) {
                                    ForEach(0...7, id: \.self) { x in
                                        PieceView(x: x, y: y, boardNumber: 0, gameManager: gm)
                                    }
                                }
                            }
                        }
                        PawnPromotionView(boardNumber: 0, color: "white", gameManager: gm, show: gm.showPawnOptions[0])
                    }
                    
                    
//                    ScrollViewReader() { value in
//                        ScrollView() {
//                            VStack(spacing: 0) {
//                                ForEach((0...29).reversed(), id: \.self) { i in
//                                    RosterSquareView(i: i, boardNumber: 0, color: "black", gameManager: gm, square: gm.roster0B[i]).onTapGesture {
//                                        gm.processRosterClick(i: i, roster: gm.roster0B, boardNumber: 0)
//                                    }
//                                }
//                                .onAppear { value.scrollTo(0) }
//                            }
//                        }
//                    }
//                    .frame(height: squareSize * 8)
                    
                    
                }


                Spacer()

                HStack(spacing: 0) {
                    
//                    ScrollView {
//                        VStack(spacing: 0) {
//                            ForEach(0...29, id: \.self) { i in
//                                RosterSquareView(i: i, boardNumber: 1, color: "black", gameManager: gm, square: gm.roster1B[i]).onTapGesture {
//                                    gm.processRosterClick(i: i, roster: gm.roster1B, boardNumber: 1)
//                                }
//                            }
//                        }
//                    }
//                    .frame(height: squareSize * 8)
                    
                    ZStack()
                    {
                        HStack(spacing: 0) {
                            ForEach((0...7).reversed(), id: \.self) { y in
                                VStack(spacing: 0) {
                                    ForEach((0...7).reversed(), id: \.self) { x in
                                        BoardSquareView(x: x, y: y, boardNumber: 1, gameManager: gm)
                                    }
                                }
                            }
                        }
                        HStack(spacing: 0) {
                            ForEach((0...7).reversed(), id: \.self) { y in
                                VStack(spacing: 0) {
                                    ForEach((0...7).reversed(), id: \.self) { x in
                                        PieceView(x: x, y: y, boardNumber: 1, gameManager: gm)
                                    }
                                }
                            }
                        }
                        PawnPromotionView(boardNumber: 1, color: "white", gameManager: gm, show: gm.showPawnOptions[1])
                    }
                    
//                    ScrollViewReader() { value in
//                        ScrollView() {
//                            VStack(spacing: 0) {
//                                ForEach((0...29).reversed(), id: \.self) { i in
//                                    RosterSquareView(i: i, boardNumber: 1, color: "white", gameManager: gm, square: gm.roster1W[i]).onTapGesture {
//                                        gm.processRosterClick(i: i, roster: gm.roster1W, boardNumber: 1)
//                                    }
//                                }
//                                .onAppear { value.scrollTo(0) }
//                            }
//                        }
//                    }
//                    .frame(height: squareSize * 8)
//                    
                }

                HStack(spacing: 0)
                {
                    TimerView(gameManager: gm, color: "black", boardNumber: 1)
                    Spacer()
                    Button("Options") {
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

    
