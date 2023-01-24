function AlcoholicAnimation(currentAlcoholState) {
    this.init = () => {
        $(".not-alcohol-friendly").each((idx, el) => {
            console.log("idx -> " + idx);
            startAnimation($(el));
        });
    }

    function startAnimation(element) {
        let animationParameters = calc(calcTheLowestBound(), calcTheUppermostBound());
        let speed = Math.random()*(500-200) + 200;
        element
            .fadeTo(speed, animationParameters.lowerOpacity)
            .fadeTo(animationParameters.upperOpacity, () => startAnimation(element));
    }

    function calcTheLowestBound() {
        return (100 - currentAlcoholState) / 100;
    }

    function calcTheUppermostBound() {
        return (calcTheLowestBound() + 100) / 2;
    }

    function calc(theLowestBound, theUppermostBound) {
        let border = (theLowestBound + theUppermostBound) / 2;
        let lowerOpacity = Math.random() * (border - theLowestBound) + theLowestBound;
        let upperOpacity = Math.random() * (theUppermostBound - border) + border;

        return {"lowerOpacity": lowerOpacity, "upperOpacity": upperOpacity};
    }
}