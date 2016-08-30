var NodeList = React.createClass({
    getInitialState: function() {
        return {data: []};
    },
    render: function() {
        return (<div>Node list</div>)
    },
    componentDidMount: function() {
        $.ajax({
            url: '/nodes',
            dataType: 'json',
            cache: false,
            success: function(data) {
                this.setState({data: data});
            }.bind(this),
            error: function(xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    }
});



ReactDOM.render(
    <NodeList />,
    document.getElementById('content')
);