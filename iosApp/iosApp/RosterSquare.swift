//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/26/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared
import Combine

class RosterSquare: ObservableObject {
    
    
    @Published var cosmetic: String = "none"
    @Published var piece: Piece = Empty()
    
    
    public func getUIImage() -> UIImage
    {
        let squareSize = UIScreen.main.bounds.width / 10
        if (piece.color == "white")
        {
            return (UIImage(named: piece.type!)?.resizeImageTo(size: CGSize(width: squareSize, height: squareSize)))!

        }
        else if (piece.color == "black")
        {
            return (UIImage(named: "b" + piece.type!)?.resizeImageTo(size: CGSize(width: squareSize, height: squareSize)))!

        }
        else
        {
            return (UIImage(named: "nothing"))!
        }
    }
    
    public func getCosmetic() -> UIImage
    {
        let squareSize = UIScreen.main.bounds.width / 10
        if (cosmetic == "yellow")
        {
            return UIColor.yellow.image(CGSize(width: squareSize, height: squareSize))
        }
        else
        {
            return (UIImage(named: "nothing"))!
        }
    }
}
extension UIColor {
    public convenience init?(hex: String) {
            let r, g, b, a: CGFloat

            if hex.hasPrefix("#") {
                let start = hex.index(hex.startIndex, offsetBy: 1)
                let hexColor = String(hex[start...])

                if hexColor.count == 8 {
                    let scanner = Scanner(string: hexColor)
                    var hexNumber: UInt64 = 0

                    if scanner.scanHexInt64(&hexNumber) {
                        r = CGFloat((hexNumber & 0xff000000) >> 24) / 255
                        g = CGFloat((hexNumber & 0x00ff0000) >> 16) / 255
                        b = CGFloat((hexNumber & 0x0000ff00) >> 8) / 255
                        a = CGFloat(hexNumber & 0x000000ff) / 255

                        self.init(red: r, green: g, blue: b, alpha: a)
                        return
                    }
                }
            }

            return nil
        }
}
