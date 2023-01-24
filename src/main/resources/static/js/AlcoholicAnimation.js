function AlcoholicAnimation(currentAlcoholState) {
    this.init = () => {
        let targetObjects = $('.not-alcohol-friendly');
        targetObjects.forEach(obj => {
           obj.fadeTo("fast", 0.5).fadeTo("fast", 1.0);
        });
    }
}