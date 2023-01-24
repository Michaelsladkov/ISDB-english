function AlcoholicAnimation(currentAlcoholState) {
    this.init = () => {
        console.log("Alcohol value -> " + currentAlcoholState);
        $(".not-alcohol-friendly").each((idx, el) => {
            startAnimation($(el));
        });
    }

    function startAnimation(element) {
        let animationParameters = calc(calcTheLowestBound(), calcTheUppermostBound());
        let speed = Math.random() * (500 - 200) + 200;
        console.log("start with parameters" + JSON.stringify(animationParameters));
        element
            .fadeTo(speed, animationParameters.lowerOpacity)
            .fadeTo(animationParameters.upperOpacity, "linear", () => startAnimation(element));
    }

    function calcTheLowestBound() {
        return (100 - currentAlcoholState) / 100;
    }

    function calcTheUppermostBound() {
        return (calcTheLowestBound() + 1) / 2;
    }

    function calc(theLowestBound, theUppermostBound) {
        let border = (theLowestBound + theUppermostBound) / 2;
        let lowerOpacity = Math.random() * (border - theLowestBound) + theLowestBound;
        let upperOpacity = Math.random() * (theUppermostBound - border) + border;

        return {"lowerOpacity": lowerOpacity, "upperOpacity": upperOpacity};
    }
}