var NodeList = React.createClass({
    getInitialState = function() {
        return [];
    }
    componentDidMount = function() {
        $.get('/nodes', function(result) {
            console.dir(result);
        })
    },
    render: function() {
        return (<div>Node list</div>)
    }
});